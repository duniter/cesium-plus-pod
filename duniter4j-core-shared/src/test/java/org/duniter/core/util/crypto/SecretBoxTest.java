package org.duniter.core.util.crypto;

/*
 * #%L
 * Duniter4j :: Core API
 * %%
 * Copyright (C) 2014 - 2015 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.duniter.core.test.TestFixtures;
import org.abstractj.kalium.keys.SigningKey;
import org.abstractj.kalium.keys.VerifyKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static org.abstractj.kalium.NaCl.Sodium.XSALSA20_POLY1305_SECRETBOX_NONCEBYTES;


public class SecretBoxTest {

	private static final Logger log = LoggerFactory.getLogger(SecretBoxTest.class);

	private TestFixtures fixtures = new TestFixtures();
	private byte[] message;
	private byte[] nonce = new byte[XSALSA20_POLY1305_SECRETBOX_NONCEBYTES];

	@Before
	public void setUp() throws UnsupportedEncodingException {
		message = "my message to encrypt !".getBytes("UTF-8");
	}

	@Test
	public void computeSeedFromSaltAndPassword()
			throws UnsupportedEncodingException {

		String salt = fixtures.getUserSalt();
		String password = fixtures.getUserPassword();
		String expectedBase64Hash = fixtures.getUserSeedHash();

		byte[] seed = SecretBox.computeSeedFromSaltAndPassword(salt, password);
		String hash = CryptoUtils.encodeBase64(seed);

		Assert.assertEquals(expectedBase64Hash, hash);
	}

	@Test
	public void getPublicKey() throws Exception {

		SecretBox secretBox = createSecretBox();

		Assert.assertEquals(fixtures.getUserPublicKey(),
				secretBox.getPublicKey());
	}
	
	@Test
	public void getSecretKey() throws Exception {

		SecretBox secretBox = createSecretBox();

		Assert.assertEquals(fixtures.getUserSecretKey(),
				secretBox.getSecretKey());
	}

	@Test
	public void encryptAndDecrypt() throws Exception {

		SecretBox secretBox = createSecretBox();

		byte[] message = "my message to encrypt !".getBytes("UTF-8");

		// encrypt
		byte[] cypherMessage = secretBox.encrypt(nonce, message);

		// decrypt
		byte[] decryptedMessage = secretBox.decrypt(nonce, cypherMessage);
		
		assertEquals(message, decryptedMessage);
	}


	@Test
	public void sign() throws AddressFormatException, UnsupportedEncodingException {

		SecretBox secretBox = createSecretBox();

		String utf8Message = String.format("UID:%s\nMETA:TS:%s\n",
				fixtures.getUid(),
				"1420881879");
		byte[] message = CryptoUtils.decodeUTF8(utf8Message);
		String expectedSignatureBase64 = "SYO77ymZmw4S3pGLxnF5zosGHdDHIEPJEKs6dM2KrFphftmnKgjmc+krq7uNsyryfNipW206BT9zRLuFFpiPBg==";

		// Call sign
		byte[] signature = secretBox.sign(message);
		String signatureBase64 = CryptoUtils.encodeBase64(signature);
		
		log.debug("expected signature: " + expectedSignatureBase64);
		log.debug("  actual signature: " + signatureBase64);

		Assert.assertEquals(
				expectedSignatureBase64,
				signatureBase64);
	}
	
	
	@Test
	public void verifyKey() throws AddressFormatException {

		SecretBox secretBox = createSecretBox();
		String pubKey = secretBox.getPublicKey();
		
		VerifyKey verifyKey = new VerifyKey(Base58.decode(pubKey));

        String message = "my message to encrypt !";
        String signature  = secretBox.sign(message);

        boolean isValidSignature = verifyKey.verify(CryptoUtils.decodeUTF8(message),
                CryptoUtils.decodeBase64(signature));
        Assert.assertTrue(isValidSignature);
	}

	/* -- Internal methods -- */

	protected static void assertEquals(byte[] expectedData, byte[] actualData) {
		Assert.assertEquals(
				CryptoUtils.encodeBase64(expectedData),
				CryptoUtils.encodeBase64(actualData));
	}

	protected SecretBox createSecretBox() {
		String salt = fixtures.getUserSalt();
		String password = fixtures.getUserPassword();
		SecretBox secretBox = new SecretBox(salt, password);

		return secretBox;
	}
	
	protected SigningKey createSigningKey() {
		String salt = fixtures.getUserSalt();
		String password = fixtures.getUserPassword();
		byte[] seed = SecretBox.computeSeedFromSaltAndPassword(salt, password);

		return new SigningKey(seed);
	}

}
