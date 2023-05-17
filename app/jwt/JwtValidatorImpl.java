/*
 * MIT License
 *
 * Copyright (c) 2017 Franz Granlund
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.crypto.SignatureData;
import com.strategyobject.substrateclient.crypto.sr25519.Sr25519NativeCryptoProvider;
import models.jpa.identity.UserIdentity;
import org.web3j.utils.Numeric;
import play.Logger;
import play.libs.F;
import reference.identity.IdentityUtils;
import reference.sr25519.Sr25519Imp;
import reference.sr25519.type.nByte;
import utils.Utils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Singleton
public class JwtValidatorImpl implements JwtValidator {

    @Inject
    public JwtValidatorImpl() {
    }

    @Override
    public F.Either<Error, VerifiedJwt> verify(String token) {
        try {
//            Logger.debug("JwtValidatorImpl: verify= "+ token);
            DecodedJWT jwtA = JWT.decode(token);
//            Logger.debug("verify: jwtA.getAlgorithm()= "+ jwtA.getAlgorithm());
//            Logger.debug("verify: jwtA.getSubject()= "+ jwtA.getSubject());
//            Logger.debug("verify: jwtA.getExpiresAt()= "+ jwtA.getExpiresAt());
//            Logger.debug("verify: jwtA.getSignature()= "+ jwtA.getSignature());

            //check algorithm
            if(!jwtA.getAlgorithm().equals(IdentityUtils.ALGORITHM_DEFAULT)) throw new AlgorithmMismatchException("The provided Algorithm doesn't match the one defined in the JWT's Header.");

            Date now= new Date();

            //check claim (not before)
            if(now.before(jwtA.getNotBefore())) return F.Either.Left(Error.ERR_NOT_BEFORE);

            //check claim (expire)
            if(now.after(jwtA.getExpiresAt())) return F.Either.Left(Error.ERR_EXPIRES_AT);

            //check signature
            byte[] signature= Numeric.hexStringToByteArray(Utils.restoreBase64String(jwtA.getSignature()));
            if(signature==null || signature.length==0) return F.Either.Left(Error.ERR_INVALID_SIGNATURE_OR_CLAIM);
//            Logger.debug("signature= "+ Numeric.toHexString(signature));
//            Logger.debug("signature.length= "+ signature.length);

            //find pubkey for user verification
            UserIdentity ui_expected= UserIdentity.findByDid(jwtA.getSubject());
            if(ui_expected==null) return F.Either.Left(Error.ERR_INVALID_SUBJECT);

            //verify signature
            String secret= (jwtA.getHeader()+"."+jwtA.getPayload());
            Sr25519Imp sr25519= new Sr25519Imp(PublicKey.fromBytes(HexConverter.toBytes(ui_expected.getPubkey())));
            boolean verify= sr25519.verify(SignatureData.fromBytes(signature), secret.getBytes(StandardCharsets.UTF_8));
            if(!verify) return F.Either.Left(Error.ERR_INVALID_SIGNATURE_VERIFY);

            return F.Either.Right(new VerifiedJwtImpl(jwtA));
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            Logger.error("f=JwtValidatorImpl, event=verify, exception=JWTVerificationException, msg={}", exception.getMessage());
            return F.Either.Left(Error.ERR_INVALID_SIGNATURE_OR_CLAIM);
        }
    }

    private boolean verifyNull(Claim claim, Object value) {
        return value == null && claim.isNull();
    }
}
