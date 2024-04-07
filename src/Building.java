import java.time.LocalDate;

public class Building {
    private int id;
    private String name;
    private boolean finished;
    private LocalDate dateOfFinishedBuild;

    public Building(int id, String name, boolean finished, LocalDate dateOfFinishedBuild) {
        this.id = id;
        this.name = name;
        this.finished = finished;
        this.dateOfFinishedBuild = dateOfFinishedBuild;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public LocalDate getDateOfFinishedBuild() {
        return dateOfFinishedBuild;
    }

    public void setDateOfFinishedBuild(LocalDate dateOfFinishedBuild) {
        this.dateOfFinishedBuild = dateOfFinishedBuild;
    }
}
