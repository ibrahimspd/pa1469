package com.example.myapplication.entites;

public class Team {
    private String teamId;
    private String name;
    private String language;
    private String kit;
    private String gkKit;
    private String mainColor;
    private String secondaryColor;
    private String teamLogo;
    private String background;
    private String lineupStyle;
    private String fontColor;
    private String font;
    private String managerId;

    public Team() {}

    private Team(TeamBuilder builder) {
        this.teamId = builder.teamId;
        this.name = builder.name;
        this.language = builder.language;
        this.kit = builder.kit;
        this.gkKit = builder.gkKit;
        this.mainColor = builder.mainColor;
        this.secondaryColor = builder.secondaryColor;
        this.teamLogo = builder.teamLogo;
        this.background = builder.background;
        this.lineupStyle = builder.lineupStyle;
        this.fontColor = builder.fontColor;
        this.font = builder.font;
        this.managerId = builder.managerId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    public String getGkKit() {
        return gkKit;
    }

    public void setGkKit(String gkKit) {
        this.gkKit = gkKit;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLineupStyle() {
        return lineupStyle;
    }

    public void setLineupStyle(String lineupStyle) {
        this.lineupStyle = lineupStyle;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Team{}";
    }

    public static class TeamBuilder {
        private String teamId;
        private String name;
        private String language;
        private String kit;
        private String gkKit;
        private String mainColor;
        private String secondaryColor;
        private String teamLogo;
        private String background;
        private String lineupStyle;
        private String fontColor;
        private String font;
        private String managerId;

        public TeamBuilder setTeamId(String teamId) {
            this.teamId = teamId;
            return this;
        }

        public TeamBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TeamBuilder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public TeamBuilder setKit(String kit) {
            this.kit = kit;
            return this;
        }

        public TeamBuilder setGkKit(String gkKit) {
            this.gkKit = gkKit;
            return this;
        }

        public TeamBuilder setMainColor(String mainColor) {
            this.mainColor = mainColor;
            return this;
        }

        public TeamBuilder setSecondaryColor(String secondaryColor) {
            this.secondaryColor = secondaryColor;
            return this;
        }

        public TeamBuilder setTeamLogo(String teamLogo) {
            this.teamLogo = teamLogo;
            return this;
        }

        public TeamBuilder setBackground(String background) {
            this.background = background;
            return this;
        }

        public TeamBuilder setLineupStyle(String lineupStyle) {
            this.lineupStyle = lineupStyle;
            return this;
        }

        public TeamBuilder setFontColor(String fontColor) {
            this.fontColor = fontColor;
            return this;
        }

        public TeamBuilder setFont(String font) {
            this.font = font;
            return this;
        }

        public TeamBuilder setManagerId(String managerId) {
            this.managerId = managerId;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }
}
