package com.tournamentmanagmentsystem.utility;

public class Utility {
    public enum Status {
        SENT("sent"), READ("read");
    
        private final String hexCode;
    
        Status(String hexCode) {
            this.hexCode = hexCode;
        }
    
        public String getHexCode() {
            return hexCode;
        }
    }
}
