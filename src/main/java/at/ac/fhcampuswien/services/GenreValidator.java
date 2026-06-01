package at.ac.fhcampuswien.services;

public enum GenreValidator {
        ACTION, DRAMA, COMEDY, SCI_FI, HORROR, THRILLER;

        public static boolean isValid(String genre) {
            for (GenreValidator g : values())
                if (g.name().equalsIgnoreCase(genre)) return true;
            return false;
        }
}
