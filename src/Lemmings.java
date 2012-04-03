import model.Model;
import view.View;

public class Lemmings {
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();

        model.setView(view);
        view.setModel(model);

        model.start();
        view.start(); /* TODO: can we launch view before model ? */
    }
}
                