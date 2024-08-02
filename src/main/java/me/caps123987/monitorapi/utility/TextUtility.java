package me.caps123987.monitorapi.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;

import java.util.List;

public class TextUtility {
    public static Component getComponentFromString(String text, boolean hasHeader, Component header) {
        Component builder = Component.text("");
        if(hasHeader)builder = builder.append(header).appendNewline();
        builder = builder.append(Component.text(text));
        return builder;
    }

    public static Component getComponentFromStringLines(List<String> text, boolean hasHeader, Component header)  {
        Component builder = Component.text("");
        if(hasHeader)builder = builder.append(header).appendNewline();

        builder = builder.append(Component.join(JoinConfiguration.separator(Component.text("\n")), text.stream().map(Component::text).toList()));

        return builder;
    }

    public static Component getComponentFromComponent(Component text, boolean hasHeader, Component header) {
        Component builder = Component.text("");
        if(hasHeader) builder = builder.append(header).appendNewline();
        builder = builder.append(text);
        return builder;
    }

    public static Component getComponentFromComponentLines(List<Component> text, boolean hasHeader, Component header)  {
        Component builder = Component.text("");

        if(hasHeader)builder = builder.append(header).appendNewline();

        builder = builder.append(Component.join(JoinConfiguration.separator(Component.text("\n")), text));

        return builder;
    }
}
