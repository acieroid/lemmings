import model.Model;
import view.View;
import controller.Controller;

public class Lemmings {
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller();

        model.addObserver(view);
        view.setController(controller);
        controller.setModel(model);
        controller.setView(view);

        view.start();
    }
}
