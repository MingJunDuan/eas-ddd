package xyz.zhangyi.ddd.eas.valueadded.employeecontext.domain.employee;

import com.google.common.base.Strings;
import xyz.zhangyi.ddd.eas.valueadded.employeecontext.domain.exception.InvalidIdCardException;
import xyz.zhangyi.ddd.eas.valueadded.employeecontext.utils.DateTimes;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDCard {
    private String number;
    private static final int LENGTH_OF_ID_CARD = 18;

    public IDCard(String number) {
        IDCardValidator idCardValidator = new IDCardValidator(number);
        idCardValidator.validate();

        this.number = number;
    }

    public String number() {
        return this.number;
    }

    public boolean isMale() {
        return getGenderCode() % 2 == 1;
    }

    public boolean isFemale() {
        return getGenderCode() % 2 == 0;
    }

    private int getGenderCode() {
        char genderFlag = number.charAt(LENGTH_OF_ID_CARD - 2);
        return Character.getNumericValue(genderFlag);
    }

    public static class IDCardValidator {
        private String number;

        IDCardValidator(String number) {
            this.number = number;
        }

        private void validate() {
            validateNullOrEmpty();
            validateLength();
            validateDigitalBits();
            validateBirthday();
            validateChecksum();
        }

        private void validateNullOrEmpty() {
            if (Strings.isNullOrEmpty(this.number)) {
                throw new InvalidIdCardException("Id card number should not be null or empty.");
            }
        }

        private void validateLength() {
            if (this.number.length() != LENGTH_OF_ID_CARD) {
                throw new InvalidIdCardException(String.format("Length of %s is not 18.", this.number));
            }
        }

        private void validateDigitalBits() {
            Pattern pattern = Pattern.compile("^[1-9]([0-9]{16})[xX0-9]$");
            Matcher matcher = pattern.matcher(this.number);
            if (!matcher.matches()) {
                throw new InvalidIdCardException(String.format("%s is not begin with digit number.", this.number));
            }
        }

        private void validateBirthday() {
            String birthdayPart = this.number.substring(6, 14);
            LocalDate minDate = LocalDate.of(1900, 1, 1);
            LocalDate maxDate = LocalDate.now();
            validateBirthday(birthdayPart, minDate, maxDate);
        }

        private void validateBirthday(String birthdayPart, LocalDate minDate, LocalDate maxDate) {
                if (!DateTimes.isValidFormat(birthdayPart, minDate, maxDate)) {
                    throw new InvalidIdCardException("Birthday of Id card is invalid.");
                }
        }

        private void validateChecksum() {
            if (calculateChecksum() != this.number.charAt(LENGTH_OF_ID_CARD - 1)) {
                throw new InvalidIdCardException(String.format("Checksum of %s is wrong", this.number));
            }
        }

        private char calculateChecksum() {
            char[] checksum = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
            int[] weightOfChecksum = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            int sum = 0;
            for (int i = 0; i < this.number.length() - 1; i++) {
                char current = this.number.charAt(i);
                sum += (current - '0') * weightOfChecksum[i];
            }
            return checksum[sum % 11];
        }
    }
}