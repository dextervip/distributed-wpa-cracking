package cliente;

public abstract class WPACracker {

    protected String capPath;
    protected String combinationPath;
    protected String status = "N/A";
    protected String currentPassphrase = "N/A";
    protected String currentTime = "N/A";
    protected String currentKeysPerSecond = "N/A";
    protected String keyFound = "N/A";

    public String getCapPath() {
        return capPath;
    }

    public String getCombinationPath() {
        return combinationPath;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentPassphrase() {
        return currentPassphrase;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getCurrentKeysPerSecond() {
        return currentKeysPerSecond;
    }

    public String getKeyFound() {
        return keyFound;
    }

    public void setCapPath(String capPath) {
        this.capPath = capPath;
    }

    public void setCombinationPath(String combinationPath) {
        this.combinationPath = combinationPath;
    }
    
    
    
    public abstract void startCrack() throws Exception;

    public abstract void stopCrack() throws Exception;
}
