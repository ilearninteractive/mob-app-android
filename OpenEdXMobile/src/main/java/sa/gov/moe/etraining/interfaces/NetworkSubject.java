package sa.gov.moe.etraining.interfaces;

public interface NetworkSubject {
    public void registerNetworkObserver(NetworkObserver observer);
    public void unregisterNetworkObserver(NetworkObserver observer);
    public void notifyNetworkDisconnect();
    public void notifyNetworkConnect();
}
