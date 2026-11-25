package frc.robot.mechanisms;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;
import org.team4159.commandsv3backport.command3.Trigger;
import org.team4159.commandsv3backport.command3.button.CommandNiDsXboxController;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Drivetrain implements Mechanism {

    private static final double NORMAL_MAX_SPEED = 0.5;
    private static final double SLOW_MAX_SPEED = 0.3;

    // declaring motor controls
    private final SparkMax leftMotor = new SparkMax(2, MotorType.kBrushless);
    private final SparkMax rightMotor = new SparkMax(1, MotorType.kBrushless);
    private SparkMaxConfig leftMotorConfig = new SparkMaxConfig();
    private SparkMaxConfig rightMotorConfig = new SparkMaxConfig();

    // max speed robot can go
    private double maxSpeed = NORMAL_MAX_SPEED;


    // only limit acceleration when slowing down - ethan zhuuu
    SlewRateLimiter filterLeft = new SlewRateLimiter(5, -10, 0);
    SlewRateLimiter filterRight = new SlewRateLimiter(5, -10, 0);

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
        double deadBand = 0.1;

        return runRepeatedly(() -> {
            maxSpeed = controller.leftBumper().getAsBoolean() ? SLOW_MAX_SPEED : NORMAL_MAX_SPEED;
            
            // debug
            System.out.println("LEFT raw" + controller.getLeftY());
            System.out.println("LEFT filtered" +
            filterLeft.calculate(MathUtil.applyDeadband(controller.getLeftY(), deadBand)));
            System.out.println("RIGHT raw" + controller.getRightY());
            System.out.println("RIGHT filtered " +
            filterRight.calculate(MathUtil.applyDeadband(controller.getRightY(),
            deadBand)));


            drive(filterLeft.calculate(MathUtil.applyDeadband(controller.getLeftY(), deadBand) * maxSpeed),
                    filterRight.calculate(MathUtil.applyDeadband(controller.getRightY(), deadBand) * maxSpeed));

        })
                .whenExited(this::stop)
                .named("Drive");
    }

    public Command autoDriveCommand(double leftSpeed, double rightSpeed) {
        return run(coroutine -> {
            drive(leftSpeed, rightSpeed);
            coroutine.park();
        })
                .whenExited(this::stop)
                .named("auto drive");
    }

}
