package frc.robot.mechanisms;

import edu.wpi.first.math.MathUtil;

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
    private final SparkMax leftMotor = new SparkMax(2, MotorType.kBrushless);
    private final SparkMax rightMotor = new SparkMax(1, MotorType.kBrushless);
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



public Command driveCommand(CommandNiDsXboxController controller) {
    double MAX_SPEED = 0.5;

    return runRepeatedly(() -> {


        drive(MathUtil.applyDeadband(-controller.getLeftY(), 0.2) * MAX_SPEED, MathUtil.applyDeadband(-controller.getRightY(), 0.2) * MAX_SPEED);

        
    })
    .whenExited(this::stop)
    .named("Drive");
}
    public Command autoDriveCommand(double leftSpeed, double rightSpeed){
        return run(coroutine -> {
            drive(leftSpeed, rightSpeed);
            coroutine.park();
    })
        .whenExited(this::stop)
        .named("auto drive");
}

}
