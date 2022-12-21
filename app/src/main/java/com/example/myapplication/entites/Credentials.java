package com.example.myapplication.entites;

public class Credentials {
    private String email;
    private String password;

    public Credentials() {}
    public Credentials(CredentialsBuilder builder) {
        this.email = builder.email;
        this.password = builder.password;
    }

    public boolean equals(Credentials credentials) {
        return this.email.equals(credentials.email) && this.password.equals(credentials.password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static class CredentialsBuilder {
        private String email;
        private String password;

        public CredentialsBuilder() {
        }

        public CredentialsBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CredentialsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public Credentials build() {
            return new Credentials(this);
        }
    }
}
