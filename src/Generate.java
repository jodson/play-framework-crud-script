import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Generate {

	private static final String PWD = System.getProperty("user.dir") + "/";
	private static final String TEMPLATES = "templates/";
	private static final String MODEL_TEMPLATE = PWD + TEMPLATES + "model_template";
	private static final String CONTROLLER_TEMPLATE = PWD + TEMPLATES + "controller_template";
	private static final String CREATE_VIEW_TEMPLATE = PWD +TEMPLATES +  "create_view_template";
	private static final String INPUT_VIEW_TEMPLATE = PWD + TEMPLATES + "input_view_template";
	private static final String EDIT_VIEW_TEMPLATE = PWD + TEMPLATES + "edit_view_template";
  private static final String SHOW_VIEW_TEMPLATE = PWD + TEMPLATES + "show_view_template";
  private static final String INDEX_VIEW_TEMPLATE = PWD + TEMPLATES + "index_view_template";
  private static final String ROUTES = PWD + "conf/routes";

	public static void main(String[] args) {
		String clazz = args[0];
		String parameters[] = args[1].split(",");
		String controller = args[2];
		String names[] = new String[parameters.length];

		String variables = "";
		for (int i = 0; i < parameters.length; i++) {
			String typeVariable[] = parameters[i].split(":");
			variables += String.format("  public %s %s;\n", typeVariable[0], typeVariable[1]);
			names[i] = typeVariable[1];
		}
		buildModel(clazz, variables);
		buildCreateView(clazz, controller, names);
		buildEditView(clazz, controller, names);
		buildController(clazz, controller, names);
		buildShowView(clazz, controller, names);
		buildIndexView(clazz, controller, names);
		addRoutes(controller);

  }

  private static void addRoutes(String controller) {
    File file = new File(ROUTES);
    String controllerLower = controller.toLowerCase();
    String routes = String.format("GET       /%s/new          controllers.%s.create()\n", controllerLower, controller);
          routes += String.format("GET       /%s/:id/edit     controllers.%s.edit(id: Long)\n", controllerLower, controller);
          routes += String.format("GET       /%s/:id/show     controllers.%s.show(id: Long)\n", controllerLower, controller);
          routes += String.format("GET       /%s/             controllers.%s.index()\n", controllerLower, controller);
          routes += String.format("GET       /%s/:id/delete   controllers.%s.delete(id: Long)\n", controllerLower, controller);
          routes += String.format("POST      /%s/save         controllers.%s.save()\n", controllerLower, controller);
          routes += String.format("POST      /%s/:id/update   controllers.%s.update(id: Long)\n", controllerLower, controller);
    PrintWriter out;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
      out.println(routes);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void buildIndexView(String clazz, String controller, String[] names) {
    File file = new File(INDEX_VIEW_TEMPLATE);
    String indexView = FileUtil.read(file);

    indexView = indexView.replaceAll("%clazz", clazz);
    indexView = indexView.replaceAll("%name", names[0]);
    indexView = indexView.replaceAll("%Controller", controller);

    File newFile = new File(String.format("%sapp/views/%s/index.scala.html", PWD, controller.toLowerCase()));
    FileUtil.write(newFile, indexView.getBytes());
    
  }
  private static void buildShowView(String clazz, String controller, String[] names) {
    File file = new File(SHOW_VIEW_TEMPLATE);
    String showView = FileUtil.read(file);

    showView = showView.replaceAll("%clazz", clazz);
    String labels = "";
    for (int i = 0; i < names.length; i++) {
      labels += String.format("<p>%s: @obj.%s</p>\n", names[i], names[i]); 
    }
    showView = showView.replaceAll("%variables", labels);

    File newFile = new File(String.format("%sapp/views/%s/show.scala.html", PWD, controller.toLowerCase()));
    FileUtil.write(newFile, showView.getBytes());
  }
  private static void buildController(String clazz, String controller, String names[]) {
    File file = new File(CONTROLLER_TEMPLATE);
    String controllerTemplate = FileUtil.read(file);

    controllerTemplate = controllerTemplate.replaceAll("%controller", controller.toLowerCase());
    controllerTemplate = controllerTemplate.replaceAll("%Controller", controller);
    controllerTemplate = controllerTemplate.replaceAll("%clazz", clazz);

    String edit = "";
    for (int i = 0; i < names.length; i++) {
      edit += String.format("found.%s = object.%s;", names[i], names[i]);
    }

    controllerTemplate = controllerTemplate.replaceAll("%edit", edit);
    File newFile = new File(String.format("%sapp/controllers/%s.java", PWD, controller));
    FileUtil.write(newFile, controllerTemplate.getBytes());
  }
  private static void buildView(String template, String filename,String clazz, String controller, String names[]) {
		File file = new File(template);
		String createView = FileUtil.read(file);

		file = new File(INPUT_VIEW_TEMPLATE);
		String inputView = FileUtil.read(file);
		
		createView = createView.replaceAll("%Controller", controller);
		createView = createView.replaceAll("%clazz", clazz);
		
		String inputs = "";
		for (int i = 0; i < names.length; i++) {
			inputs += inputView.replaceAll("%name", names[i]);
		}
		createView = createView.replaceAll("%inputs", inputs);
		
		String dir = String.format("%sapp/views/%s/", PWD, controller.toLowerCase());
		File newDirectory = new File(dir);
		if(!newDirectory.exists()) {
			newDirectory.mkdirs();
		}
		File newFile = new File(String.format("%s%s.scala.html", dir, filename));
		FileUtil.write(newFile, createView.getBytes());
	}
	private static void buildCreateView(String clazz, String controller, String[] names) {
		buildView(CREATE_VIEW_TEMPLATE, "create", clazz, controller, names);
	}

	private static void buildEditView(String clazz, String controller, String[] names) {
		buildView(EDIT_VIEW_TEMPLATE, "edit",clazz, controller, names);
	}

	public static void buildModel(String clazz, String variables) {
		File file = new File(MODEL_TEMPLATE);
		String modelTemplate = FileUtil.read(file);

		modelTemplate = modelTemplate.replaceAll("%clazz", clazz);
		modelTemplate = modelTemplate.replaceAll("%variables", variables);

		File newFile = new File(String.format("%sapp/models/%s.java", PWD, clazz));
		FileUtil.write(newFile, modelTemplate.getBytes());
	}
	
}
