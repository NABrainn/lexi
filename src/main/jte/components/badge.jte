@param String badge
@param String position
@param gg.jte.Content content

<div class="badge ${"before".equals(position) ? "before" : "after"}">
    ${content}
    <span class="badge__chip" aria-label="Powiadomienia: ${badge}">
        ${badge}
    </span>
</div>