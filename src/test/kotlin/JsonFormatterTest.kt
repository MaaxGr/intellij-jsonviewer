import com.maaxgr.intellij.jsonviewer.JsonFormatter
import org.junit.Test
import kotlin.test.assertEquals

class JsonFormatterTest {

    @Test
    fun json_format_1() {
        val testString = """{"fruit": "Apple","size": "Large","color": "Red"}"""
        val expectedString = """
            {
              "fruit": "Apple",
              "size": "Large",
              "color": "Red"
            }
        """.trimIndent()


        val formatter = JsonFormatter()
        val formattedString = formatter.format(testString)

        assertEquals(expectedString, formattedString)
    }

    @Test
    fun json_format_2() {
        val testString = """{"fruit": "App]le","size": "Lar}ge","color": "Re:d"}"""
        val expectedString = """
            {
              "fruit": "App]le",
              "size": "Lar}ge",
              "color": "Re:d"
            }
        """.trimIndent()


        val formatter = JsonFormatter()
        val formattedString = formatter.format(testString)

        assertEquals(expectedString, formattedString)
    }

    @Test
    fun test_format_3() {
        val testString = """{"quiz":{"sport":{"q1":{"question":"Which one is correct team name in NBA?","options":["New York Bulls","Los Angeles Kings","Golden State Warriros","Huston Rocket"],"answer":"Huston Rocket"}}}}"""
        val expectedString = """
            {
              "quiz": {
                "sport": {
                  "q1": {
                    "question": "Which one is correct team name in NBA?",
                    "options": [
                      "New York Bulls",
                      "Los Angeles Kings",
                      "Golden State Warriros",
                      "Huston Rocket"
                    ],
                    "answer": "Huston Rocket"
                  }
                }
              }
            }
        """.trimIndent()

        val formatter = JsonFormatter()
        val formattedString = formatter.format(testString)

        assertEquals(expectedString, formattedString)
    }

    @Test
    fun reset() {
        val formatter = JsonFormatter()

        val testString = """
            {
              "quiz":{
                "sport":{
                  "q1":{
                    "question":"Which one is correct team name in NBA?",
                    "options":[
                      "New York \"Bulls",
                      "Los Angeles {Kings",
                      "Golden State Warriros",
                      "Huston Rocket"
                    ],
                    "answer":"Huston Rocket"
                  }
                }
              }
            }
        """.trimIndent()

        val result = formatter.resetString(testString)


        println(result)
    }


}
