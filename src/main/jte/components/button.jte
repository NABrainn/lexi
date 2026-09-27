@param String text = null
@param String href = null
@param String kind = "filled"
@param String variant = "primary"
@param gg.jte.Content icon = null
@param boolean disabled = false
@param String type = "submit"

!{
    String buttonKind = kind.toLowerCase(java.util.Locale.ROOT)
        .trim()
        .replace(" ", "-");

    String buttonVariant = switch (variant.toLowerCase(java.util.Locale.ROOT)) {
        case "secondary", "tertiary", "error" -> variant.toLowerCase(java.util.Locale.ROOT);
        default -> "primary";
    };

    boolean iconOnly = buttonKind.equals("icon")
        || buttonKind.equals("fab")
        || buttonKind.equals("mini-fab");

    boolean isLink = href != null;
}
@if(isLink && !disabled)
    <a
        class="material-button ${buttonKind} ${buttonVariant}"
        href="${href}"
        aria-label="${iconOnly ? text : null}"
    >
        @if(icon != null)
            <span class="material-button__icon" aria-hidden="true">${icon}</span>
        @endif
        @if(!iconOnly && text != null)
            <span>${text}</span>
        @endif
    </a>
@else
    <button
        class="material-button ${buttonKind} ${buttonVariant}"
        type="${type}"
        disabled="${disabled}"
        aria-label="${iconOnly ? text : null}"
    >
        @if(icon != null)
            <span class="material-button__icon" aria-hidden="true">${icon}</span>
        @endif
        @if(!iconOnly && text != null)
            <span>${text}</span>
        @endif
    </button>
@endif