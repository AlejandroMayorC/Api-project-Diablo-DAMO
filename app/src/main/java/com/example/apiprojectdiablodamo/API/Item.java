package com.example.apiprojectdiablodamo.API;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {
    private String id;
    private String slug;
    private String name;
    private String icon;
    private String tooltipParams;
    private int requiredLevel;
    private boolean accountBound;
    private String flavorText;
    private String typeName;
    private ItemType type;
    private String damage;
    private String dps;
    private String color;
    private boolean isSeasonRequiredToDrop;
    private int seasonRequiredToDrop;
    private List<String> slots;
    private Attributes attributes;
    private List<RandomAffix> randomAffixes;
    private List<?> setItems;
    private boolean esPreferit;

    public Item(String id, String slug, String name, String icon, String tooltipParams, int requiredLevel, boolean accountBound, String flavorText, String typeName, ItemType type, String damage, String dps, String color, boolean isSeasonRequiredToDrop, int seasonRequiredToDrop, List<String> slots, Attributes attributes, List<RandomAffix> randomAffixes, List<?> setItems) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.icon = icon;
        this.tooltipParams = tooltipParams;
        this.requiredLevel = requiredLevel;
        this.accountBound = accountBound;
        this.flavorText = flavorText;
        this.typeName = typeName;
        this.type = type;
        this.damage = damage;
        this.dps = dps;
        this.color = color;
        this.isSeasonRequiredToDrop = isSeasonRequiredToDrop;
        this.seasonRequiredToDrop = seasonRequiredToDrop;
        this.slots = slots;
        this.attributes = attributes;
        this.randomAffixes = randomAffixes;
        this.setItems = setItems;
    }

    public Item() {
        // Pot ser buit o inicialitzar valors predeterminats si és necessari
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTooltipParams() {
        return tooltipParams;
    }

    public void setTooltipParams(String tooltipParams) {
        this.tooltipParams = tooltipParams;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public boolean isAccountBound() {
        return accountBound;
    }

    public void setAccountBound(boolean accountBound) {
        this.accountBound = accountBound;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getDps() {
        return dps;
    }

    public void setDps(String dps) {
        this.dps = dps;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getIsSeasonRequiredToDrop() {
        return isSeasonRequiredToDrop;
    }

    public void setIsSeasonRequiredToDrop(boolean seasonRequiredToDrop) {
        isSeasonRequiredToDrop = seasonRequiredToDrop;
    }

    public int getSeasonRequiredToDrop() {
        return seasonRequiredToDrop;
    }

    public void setSeasonRequiredToDrop(int seasonRequiredToDrop) {
        this.seasonRequiredToDrop = seasonRequiredToDrop;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<RandomAffix> getRandomAffixes() {
        return randomAffixes;
    }

    public void setRandomAffixes(List<RandomAffix> randomAffixes) {
        this.randomAffixes = randomAffixes;
    }

    public List<?> getSetItems() {
        return setItems;
    }

    public void setSetItems(List<?> setItems) {
        this.setItems = setItems;
    }

    public boolean getPreferit() {
        return esPreferit;
    }

    public void setPreferit(boolean esPreferit) {
        this.esPreferit = esPreferit;
    }

    public static class ItemType implements Serializable {
        private boolean twoHanded;
        private String id;

        public ItemType(boolean twoHanded, String id) {
            this.twoHanded = twoHanded;
            this.id = id;
        }

        public boolean isTwoHanded() {
            return twoHanded;
        }

        public void setTwoHanded(boolean twoHanded) {
            this.twoHanded = twoHanded;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        // Getters y setters...
    }

    public static class Attributes implements Serializable {
        private List<Attribute> primary;
        private List<Attribute> secondary;
        private List<?> other;

        public Attributes(List<Attribute> primary, List<Attribute> secondary, List<?> other) {
            this.primary = primary;
            this.secondary = secondary;
            this.other = other;
        }

        public List<Attribute> getPrimary() {
            return primary;
        }

        public void setPrimary(List<Attribute> primary) {
            this.primary = primary;
        }

        public List<Attribute> getSecondary() {
            return secondary;
        }

        public void setSecondary(List<Attribute> secondary) {
            this.secondary = secondary;
        }

        public List<?> getOther() {
            return other;
        }

        public void setOther(List<?> other) {
            this.other = other;
        }

        // Getters y setters...
    }

    public static class Attribute implements Serializable {
        private String textHtml;
        private String text;

        public Attribute(String textHtml, String text) {
            this.textHtml = textHtml;
            this.text = text;
        }

        public String getTextHtml() {
            return textHtml;
        }

        public void setTextHtml(String textHtml) {
            this.textHtml = textHtml;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class RandomAffix implements Serializable {
        private List<Attribute> oneOf;

        public RandomAffix(List<Attribute> oneOf) {
            this.oneOf = oneOf;
        }

        public List<Attribute> getOneOf() {
            return oneOf;
        }

        public void setOneOf(List<Attribute> oneOf) {
            this.oneOf = oneOf;
        }

    }
}
