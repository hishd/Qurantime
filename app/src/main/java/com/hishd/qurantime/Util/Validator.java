package com.hishd.qurantime.Util;

import org.jetbrains.annotations.NotNull;

public abstract class Validator {
    final static String USER_NAME_PATTERN = "^[a-zA-Z0-9]{3,50}$";
    final static String NAME_PATTERN = "^[a-zA-Z ]{3,50}$";
    final static String NICK_NAME_PATTERN = "^[a-zA-Z]{2,20}$";
    //    final static String PHONE_PATTERN = "^[0-9]{5,20}$";
    final static String PHONE_PATTERN_FULL = "^[07]+[0-9]{8}$";
    final static String PHONE_PATTERN = "^[7]+[0-9]{8}$";
    final static String NUMBER_AMOUNT_PATTERN = "^[0-9]{1,25}$";
    final static String OTP_PATTERN = "^[0-9]{4}$";
    final static String NUMBER_PATTERN = "^[0-9]{1,30}$";
    final static String EMAIL_PATTERN = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    final static String NUM_CHAR_PATTERN = "^[a-zA-Z0-9]{1,50}$";
    final static String NIC_PATTERN = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$";
    //One Number, One Lowercase, One Uppercase, One Special Char, Min Length 6, Max Length 20
    final static String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$";
    final static String URL_PATTERN = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

    public static boolean isValidNickName(@NotNull String name) {
        return name.matches(NICK_NAME_PATTERN);
    }

    public static boolean isValidName(@NotNull String name) {
        return name.matches(NAME_PATTERN);
    }

    public static boolean isValidAmount(@NotNull String amount, boolean isAmount) {
        if (isAmount)
            return amount.matches(NUMBER_AMOUNT_PATTERN);
        return amount.matches(NUMBER_PATTERN);
    }

    public static boolean isValidUserName(@NotNull String name) {
        return name.matches(USER_NAME_PATTERN);
    }

    public static boolean isValidPhone(@NotNull String phone, boolean inFullFormat) {
        if (inFullFormat)
            return phone.matches(PHONE_PATTERN_FULL);

        return phone.matches(PHONE_PATTERN);
    }

    public static boolean isValidEmail(@NotNull String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isValidPolicyNo(@NotNull String policyNo) {
        return policyNo.matches(NUM_CHAR_PATTERN);
    }

    public static boolean isValidNIC(@NotNull String nicNo){
        return nicNo.matches(NIC_PATTERN);
    }

    public static boolean isValidPassword(@NotNull String password){
        return password.matches(PASSWORD_PATTERN);
    }

    public static boolean isValidURL(@NotNull String url){
        return url.matches(URL_PATTERN);
    }

    public static boolean isValidOTP(@NotNull String otp){
        return otp.matches(OTP_PATTERN);
    }
}
