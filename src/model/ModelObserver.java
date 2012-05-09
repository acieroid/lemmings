package model;

public interface ModelObserver {
    public void setModel(Model model);
    public void characterAdded(Character character);
    public void characterChanged(Character character, int change);
    public void charactersCleared();
    public void destroyed(int x, int y, int w, int h);
    public void destroyed(int zone[], int x, int y, int w, int h);
    public void speedChanged();
    public void finished();
}