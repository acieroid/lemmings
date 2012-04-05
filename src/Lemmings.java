import model.Model;
import view.View;
import controller.Controller;

public class Lemmings {
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller();

        model.setView(view);
        view.setModel(model);
        view.setController(controller);
        controller.setModel(model);

        view.start();
    }
}
                