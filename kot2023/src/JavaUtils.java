import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {

    public static List<String> matchCalibrations(String line) {
        final String regex = "(?=(\\d|one|two|three|four|five|six|seven|eight|nine|zero))";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(line);

        String first = null;
        String last = null;

        while (matcher.find()) {
            var value = matcher.group(1);
            if (first == null) {
                first = value;
            }
            last =value;
        }
        return List.of(first, last);
    }

    public static void main(String[] args) {
        var x = matchCalibrations("fbbdeighthreezzsdffh8jbjzxkclj");
        System.out.println(x);
    }
}