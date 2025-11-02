package bloodnet.storage;

import static bloodnet.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalPersons.BENSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@.";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_BLOOD_TYPE = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_DATE_OF_BIRTH = "XX/11/1955";

    private static final String VALID_UUID = "c633e619-304e-4d8f-ad18-8c4694ad05fd";
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_BLOOD_TYPE = BENSON.getBloodType().toString();
    private static final String VALID_DATE_OF_BIRTH = BENSON.getDateOfBirth().toString();

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_UUID, INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_BLOOD_TYPE,
                        VALID_DATE_OF_BIRTH);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_UUID, null, VALID_PHONE, VALID_EMAIL,
                VALID_BLOOD_TYPE, VALID_DATE_OF_BIRTH);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_UUID, VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_BLOOD_TYPE,
                        VALID_DATE_OF_BIRTH);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_UUID, VALID_NAME, null, VALID_EMAIL,
                VALID_BLOOD_TYPE, VALID_DATE_OF_BIRTH);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_UUID, VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_BLOOD_TYPE,
                        VALID_DATE_OF_BIRTH);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_UUID, VALID_NAME, VALID_PHONE, null,
                VALID_BLOOD_TYPE,
                VALID_DATE_OF_BIRTH);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidBloodType_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_UUID, VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_BLOOD_TYPE,
                        VALID_DATE_OF_BIRTH);
        String expectedMessage = BloodType.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullBloodType_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_UUID, VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_DATE_OF_BIRTH);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, BloodType.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullDateOfBirth_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_UUID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_BLOOD_TYPE,
                null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, DateOfBirth.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidDateofBirth_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_UUID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_BLOOD_TYPE, INVALID_DATE_OF_BIRTH);
        String expectedMessage = DateOfBirth.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }


}
