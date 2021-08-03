package bugbusters.everyonecodes.java.Registration.Validation.Password;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(6, 15), //min 6 and max 15 characters
                new CharacterRule(EnglishCharacterData.UpperCase, 1), //min 1 uppercase character
                new CharacterRule(EnglishCharacterData.LowerCase, 1), //min 1 lowercase character
                new CharacterRule(EnglishCharacterData.Digit, 1), //min 1 number
                new CharacterRule(EnglishCharacterData.Special, 1), //min 1 special character
                new WhitespaceRule() //no whitespace
        ));
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream()
                .collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}


