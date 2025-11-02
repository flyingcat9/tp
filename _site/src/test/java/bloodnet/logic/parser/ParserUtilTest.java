package bloodnet.logic.parser;

import static bloodnet.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Phone;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R23";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_BLOOD_TYPE = " ";
    private static final String INVALID_DATE_OF_BIRTH = "XX-20-1000";
    private static final String INVALID_EMAIL = "example.com";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "88888888";
    private static final String VALID_BLOOD_TYPE = "B+";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_DATE_OF_BIRTH = "10-12-1998";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseBloodType_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseBloodType((String) null));
    }

    @Test
    public void parseBloodType_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseBloodType(INVALID_BLOOD_TYPE));
    }

    @Test
    public void parseBloodType_validValueWithoutWhitespace_returnsBloodType() throws Exception {
        BloodType expectedBloodType = new BloodType(VALID_BLOOD_TYPE);
        assertEquals(expectedBloodType, ParserUtil.parseBloodType(VALID_BLOOD_TYPE));
    }

    @Test
    public void parseBloodType_validValueWithWhitespace_returnsTrimmedBloodType() throws Exception {
        String bloodTypeWithWhitespace = WHITESPACE + VALID_BLOOD_TYPE + WHITESPACE;
        BloodType expectedBloodType = new BloodType(VALID_BLOOD_TYPE);
        assertEquals(expectedBloodType, ParserUtil.parseBloodType(bloodTypeWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    /**
     * Checking to see what is returned if null is returned as a birthdate.
     */
    @Test
    public void parseDateOfBirth_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDateOfBirth((String) null));
    }

    @Test
    public void parseDateOfBirth_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDateOfBirth(INVALID_DATE_OF_BIRTH));
    }

    @Test
    public void parseDateOfBirth_validValueWithoutWhitespace_returnsDateOfBirth() throws Exception {
        DateOfBirth expectedDateOfBirth = new DateOfBirth(VALID_DATE_OF_BIRTH);
        assertEquals(expectedDateOfBirth, ParserUtil.parseDateOfBirth(VALID_DATE_OF_BIRTH));
    }

    @Test
    public void parseDateOfBirth_validValueWithWhitespace_returnsDateOfBirth() throws Exception {
        String dateOfBirthWithWhiteSpace = WHITESPACE + VALID_DATE_OF_BIRTH + WHITESPACE;
        DateOfBirth expectedDateOfBirth = new DateOfBirth(VALID_DATE_OF_BIRTH);
        assertEquals(expectedDateOfBirth, ParserUtil.parseDateOfBirth(dateOfBirthWithWhiteSpace));
    }

    @Test
    public void parseDonationDate_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDonationDate((String) null));
    }

    @Test
    public void parseDonationDate_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDonationDate("28 Jun 2026"));
    }

    @Test
    public void parseDonationDate_validValueWithoutWhitespace_returnsDonationDate() throws Exception {
        DonationDate expectedDonationDate = new DonationDate("10-12-2023");
        assertEquals(expectedDonationDate, ParserUtil.parseDonationDate("10-12-2023"));
    }

    @Test
    public void parseDonationDate_validValueWithWhitespace_returnsTrimmedDonationDate() throws Exception {
        String donationDateWithWhitespace = WHITESPACE + "10-12-2023" + WHITESPACE;
        DonationDate expectedDonationDate = new DonationDate("10-12-2023");
        assertEquals(expectedDonationDate, ParserUtil.parseDonationDate(donationDateWithWhitespace));
    }

    @Test
    public void parseBloodVolume_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseBloodVolume((String) null));
    }

    @Test
    public void parseBloodVolume_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseBloodVolume("invalid"));
    }

    @Test
    public void parseBloodVolume_validValueWithoutWhitespace_returnsBloodVolume() throws Exception {
        BloodVolume expectedBloodVolume = new BloodVolume("450");
        assertEquals(expectedBloodVolume, ParserUtil.parseBloodVolume("450"));
    }

    @Test
    public void parseBloodVolume_validValueWithWhitespace_returnsTrimmedBloodVolume() throws Exception {
        String bloodVolumeWithWhitespace = WHITESPACE + "450" + WHITESPACE;
        BloodVolume expectedBloodVolume = new BloodVolume("450");
        assertEquals(expectedBloodVolume, ParserUtil.parseBloodVolume(bloodVolumeWithWhitespace));
    }

}
