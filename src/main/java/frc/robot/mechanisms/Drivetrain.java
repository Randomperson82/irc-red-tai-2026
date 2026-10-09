package frc.robot.mechanisms;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;
import org.team4159.commandsv3backport.command3.Trigger;
import org.team4159.commandsv3backport.command3.button.CommandNiDsXboxController;
import org.team4159.commandsv3backport.event.EventLoop;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Drivetrain implements Mechanism {

    // declaring motor controls
    private final SparkMax leftMotor = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax rightMotor = new SparkMax(2, MotorType.kBrushless);
    private SparkMaxConfig leftMotorConfig = new SparkMaxConfig();
    private SparkMaxConfig rightMotorConfig = new SparkMaxConfig();

    
    public Drivetrain() {
        leftMotorConfig.inverted(true);
        leftMotorConfig.smartCurrentLimit(40);
        leftMotorConfig.secondaryCurrentLimit(80);
        leftMotor.configure(leftMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        rightMotorConfig.inverted(false);
        rightMotorConfig.smartCurrentLimit(40);
        rightMotorConfig.secondaryCurrentLimit(80);
        rightMotor.configure(rightMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);


    }

    private void drive(double leftSpeed, double rightSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
    }
    private void stop() {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }
    private static final double MAX_SPEED = 1.0;
    private static final double AIMING_TURN_SPEED = 0.2;



public Command driveCommand(CommandNiDsXboxController controller) {
    return runRepeatedly(() -> {
        //TO DO
        // omg you dont understand how much time this took
        double turnSpeed = (controller.getLeftTriggerAxis() - controller.getRightTriggerAxis()) * AIMING_TURN_SPEED;

        double leftJoyStick = controller.getLeftY() * MAX_SPEED;
        double rightJoyStick = controller.getRightY() * MAX_SPEED;

        // turn left and subtract rigth side so one side is spinning the other direction
        double left = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, leftJoyStick + turnSpeed));
        double right = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, rightJoyStick - turnSpeed));

        drive(left, right);

        
    })
    .whenExited(this::stop)
    .named("Drive");
}
}