package mazegame.entity;


import java.util.List;

public class DescriptionBuilder {
    private final StringBuilder sb = new StringBuilder();

    public DescriptionBuilder line(String text) {
        sb.append(text).append("\n");
        return this;
    }

    public DescriptionBuilder section(String title, String content) {
        if (content != null && !content.isBlank()) {
            sb.append(title).append(" ").append(content).append("\n");
        }
        return this;
    }

    public DescriptionBuilder sectionBlock(String title, List<String> lines) {
        if (lines != null && !lines.isEmpty()) {
            sb.append(title).append("\n");
            for (String l : lines) {
                if (l != null && !l.isBlank()) {
                    sb.append(l).append("\n");
                }
            }
        }
        return this;
    }

    public String build() {
        return sb.toString();
    }
}
