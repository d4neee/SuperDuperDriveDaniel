package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    CredentialMapper credentialMapper;
    EncryptionService encryptionService;

    public CredentialService(EncryptionService encryptionService, CredentialMapper credentialMapper) {
        this.encryptionService = encryptionService;
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getAllCredentials(int credentialId) {
        List<Credential> credentialList = credentialMapper.getAllCredentials(credentialId);

        // Need to grab the decrypted passwords because they may need to be displayed.
        for (Credential credential : credentialList) {
            credential.setDecryptedPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        }

        return credentialList;
    }

    public int insertCredential(Credential credential) {
        int id;

        // Need to create a key and encrypt the password.
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];

        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        // set the newly created key and encrypted password.
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);

        if (credential.getCredentialId() == null) {
            id = credentialMapper.insertCredential(credential);
        } else { // an id exists so we are just editing an existing credential
            id = credentialMapper.updateCredential(credential);
        }
        return id;
    }

    public void deleteCredential(int id) {
        credentialMapper.deleteCredential(id);
    }

}
