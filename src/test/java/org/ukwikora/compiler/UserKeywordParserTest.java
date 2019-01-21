package org.ukwikora.compiler;

import org.ukwikora.model.UserKeyword;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

public class UserKeywordParserTest {
    @Test
    public void checkTypicalKeyword() throws IOException {
        String text = "I create a transfer of <${amount}> to an other account with 3 letters name\n" +
                "    [Documentation]    Click on button \"Make a transfer\", select the debit account, choose to create a new beneficiary with 3 letters name as credit account, create the transfer by entering all the fields and validate with a good card code   \n" +
                "    I'm on \"Payment\" board \n" +
                "    I click on \"Make a Transfer\" \n" +
                "    I select the debit account for the transfer\n" +
                "    Check \"other account\" tab is opened\n" +
                "    I fill out all the beneficiary informations    ${IBAN_LU_Other_account}    ${Beneficiary_Name_3_characters}\n" +
                "    Go to the page to create the transfer \n" +
                "    I fill out the Amount of <${amount}> and the others fields\n" +
                "    I submit the order for an other account with 3 letters name \n" +
                "    Valider la saisie du card code    ${DigiCode-Num1}    ${DigiCode-Num2}    ${DigiCode-Num3}";

        LineReader reader = createLineReader(text);

        UserKeyword keyword = null;

        try {
            keyword = UserKeywordParser.parse(reader);
        } catch (IOException e) {
            fail("exception caught: " + e.getMessage());
        }

        assertNotNull(keyword);

        assertEquals("I create a transfer of <${amount}> to an other account with 3 letters name", keyword.getName());
        assertEquals("Click on button \"Make a transfer\", select the debit account, choose to create a new beneficiary with 3 letters name as credit account, create the transfer by entering all the fields and validate with a good card code", keyword.getDocumentation().trim());
        assertEquals(9, keyword.getSteps().size());
    }

    LineReader createLineReader(String text) throws IOException {
        Reader targetReader = new StringReader(text);
        LineReader reader = new LineReader(targetReader);
        reader.readLine();

        return reader;
    }
}
