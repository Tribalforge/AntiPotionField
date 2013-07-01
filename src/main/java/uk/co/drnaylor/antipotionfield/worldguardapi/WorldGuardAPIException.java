package uk.co.drnaylor.antipotionfield.worldguardapi;

public class WorldGuardAPIException extends Exception {

    public enum WorldGuardExceptions {
        NotEnabled
    }

    public WorldGuardExceptions except;

    public WorldGuardAPIException() {
        except = WorldGuardExceptions.NotEnabled;
    }
}
