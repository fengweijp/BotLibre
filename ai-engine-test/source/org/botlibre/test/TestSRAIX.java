/******************************************************************************
 *
 *  Copyright 2014 Paphus Solutions Inc.
 *
 *  Licensed under the Eclipse Public License, Version 1.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/
package org.botlibre.test;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.botlibre.Bot;
import org.botlibre.knowledge.Bootstrap;
import org.botlibre.sense.service.RemoteService;
import org.botlibre.sense.text.TextEntry;
import org.botlibre.thought.language.Language;
import org.botlibre.thought.language.Language.LearningMode;
import org.botlibre.util.Utils;

/**
 * Test AIML2 SRAIX support.
 */

public class TestSRAIX extends TextTest {
	
	public boolean isChatLog() {
		return false;
	}

	@BeforeClass
	public static void setup() {
		reset();
		new Bootstrap().bootstrapSystem(bot, false);
		Bot bot = Bot.createInstance();
		try {
			URL url = TestAIML.class.getResource("test-aiml2.aiml");
			File file = new File(url.toURI());
			bot.mind().getThought(Language.class).loadAIMLFile(file, true, false, "");
		} catch (Exception exception) {
			fail(exception.toString());
		}
		
		Utils.sleep(5000);
		
		bot.shutdown();
		RemoteService.SERVER = "http://localhost:9080/botlibre";
	}

	@org.junit.Test
	public void testSRAIX() {
		Bot bot = Bot.createInstance();
		Language language = bot.mind().getThought(Language.class);
		language.setLearningMode(LearningMode.Disabled);
		TextEntry text = bot.awareness().getSense(TextEntry.class);
		List<String> output = registerForOutput(text);
		//bot.setDebugLevel(Level.FINER);
		
		text.input("sraix what is love");
		String response = waitForOutput(output);
		if (response.indexOf("Love is a variety of different feelings") == -1) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixkey 2+2");
		response = waitForOutput(output);
		if (!response.equals("4")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixlimit what is love");
		response = waitForOutput(output);
		if (!response.equals("Love is a variety of different feelings, states, and attitudes that ranges from interpersonal affection (\"I love my mother\") to pleasure (\"I loved that meal\").")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixchomsky hello");
		response = waitForOutput(output);
		if (response.indexOf("Hello") == -1 && response.indexOf("Hi") == -1) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixchomsky2 hello");
		response = waitForOutput(output);
		if (response.indexOf("Hello") == -1 && response.indexOf("Hi") == -1) {
			fail("Incorrect response: " + response);
		}
		
		text.input("pannous 2+2");
		response = waitForOutput(output);
		if (!response.equals("4")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixbrainbot 2+2");
		response = waitForOutput(output);
		if (!response.equals("2 + 2 = 4")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixbrainbot2 2+2");
		response = waitForOutput(output);
		if (!response.equals("2 + 2 = 4")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixbrainbot3 2+2");
		response = waitForOutput(output);
		if (!response.equals("2 + 2 = 4")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixbrainbot4 2+2");
		response = waitForOutput(output);
		if (!response.equals("Error")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("sraixbrainbot5 2+2");
		response = waitForOutput(output);
		if (!response.equals("2 + 2 = 4")) {
			fail("Incorrect response: " + response);
		}
	}

	@org.junit.Test
	public void testServices() {
		Bot bot = Bot.createInstance();
		Language language = bot.mind().getThought(Language.class);
		language.setLearningMode(LearningMode.Disabled);
		TextEntry text = bot.awareness().getSense(TextEntry.class);
		List<String> output = registerForOutput(text);
		//bot.setDebugLevel(Level.FINER);
		
		text.input("wikidata Ottawa");
		String response = waitForOutput(output);
		if (response.indexOf("capital city of Canada") == -1) {
			fail("Incorrect response: " + response);
		}
		
		text.input("wikidatacountry Ottawa");
		response = waitForOutput(output);
		if (!response.equals("Canada")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("freebase Toronto");
		response = waitForOutput(output);
		if (response.indexOf("Toronto is the most populous city in Canada and the provincial capital of Ontario.") == -1) {
			fail("Incorrect response: " + response);
		}
		
		text.input("wikidatacountry Toronto");
		response = waitForOutput(output);
		if (!response.equals("Canada")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("wiktionary love");
		response = waitForOutput(output);
		if (!response.equals("Strong affection.")) {
			fail("Incorrect response: " + response);
		}
		
		text.input("wikidata Toronto hint country");
		response = waitForOutput(output);
		if (!response.equals("Canada")) {
			fail("Incorrect response: " + response);
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		shutdown();
	}
}

