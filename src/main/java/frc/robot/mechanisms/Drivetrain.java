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
        rightMotor.configure(leftMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);


    }

    private void drive(double leftSpeed, double rightSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
    }
    private void stop() {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }


public Command driveCommand(CommandNiDsXboxController controller) {
    return runRepeatedly(() -> {
        drive(controller.getLeftY() * 0.2, controller.getRightY() * 0.2);
        
    })
    .whenExited(this::stop)
    .named("Drive");
}

}