package frc.robot.subsystems;

public enum ElevatorState {
    NONE (0),
    LIMIT_UP (1),
    LIMIT_DOWN (-1);

    private int position = 0;
    
    private ElevatorState(int p) {
        position = p;
    }

    public int getPosition() {
        return position;
    }
}