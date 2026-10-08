package frc.robot.mechanisms;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;
import org.team4159.commandsv3backport.command3.button.CommandNiDsXboxController;

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


public Command driveCommand(CommandNiDsXboxController controller) {
    return runRepeatedly(() -> {

        //omg this part took like an hour to figure out :sob:

        // gets output of left joystick y axis so both wheels could go forward or backwards
        double move = controller.getLeftY() * MAX_SPEED;
        // gets output of left joystick x axis to determine where to turn
        double turn = controller.getLeftX() * MAX_SPEED;
        drive(move - turn, move + turn);
        
    })
    .whenExited(this::stop)
    .named("Drive");
}
}