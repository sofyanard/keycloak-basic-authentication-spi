package com.sofyanard.myspitest;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.events.Errors;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordUserCredentialModel;
import org.keycloak.models.credential.OTPCredentialModel;
import org.keycloak.models.utils.HmacOTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySpiTest  implements Authenticator {

    private static final Logger log = LoggerFactory.getLogger(MySpiTest.class);

    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {

        // reset previously failed flow
        authenticationFlowContext.resetFlow();

        log.info("just want to write a log !!!");

        // generate QR
        final String totpSecret = HmacOTP.generateSecret(20);
        log.info("totpSecret = {}", totpSecret);

        // get user
        UserModel user = authenticationFlowContext.getUser();
        log.info("user = {}", user.getUsername());

        // get realm
        RealmModel realm = authenticationFlowContext.getRealm();
        log.info("realm = {}", realm.getId());

        // get totp is configured or not
        KeycloakSession keycloakSession = authenticationFlowContext.getSession();
        if (keycloakSession != null) {
            boolean isTotpConfigured = keycloakSession.userCredentialManager().isConfiguredFor(realm, user, OTPCredentialModel.TYPE);
            log.info("isTotpConfigured = {}", isTotpConfigured);
        }
        else {
            log.error("Keycloak session is null");
        }

        authenticationFlowContext.success();;
    }

    public void action(AuthenticationFlowContext authenticationFlowContext) {
        authenticationFlowContext.success();
    }

    public boolean requiresUser() {
        return false;
    }

    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return false;
    }

    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    public void close() {

    }

}