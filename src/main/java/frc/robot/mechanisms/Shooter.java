package frc.robot.mechanisms;


import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

public class Shooter implements Mechanism {
  private final SparkMax motor1 = new SparkMax(3, MotorType.kBrushless);
  private final SparkMax motor2 = new SparkMax(4, MotorType.kBrushless);

  private SparkMaxConfig motorConfig1 = new SparkMaxConfig();
  private SparkMaxConfig motorConfig2 = new SparkMaxConfig();
  public Shooter () {
    motorConfig1.inverted(true);
    motorConfig1.smartCurrentLimit(40);
    motorConfig1.secondaryCurrentLimit(80);
    motor1.configure(motorConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    motorConfig2.inverted(false);
    motorConfig2.smartCurrentLimit(40);
    motorConfig2.secondaryCurrentLimit(80);
    motor2.configure(motorConfig2, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
  }
  
  private void stop() {
    motor2.stopMotor();
    motor1.stopMotor();
  }

  private void shoot(double speed) {
    motor1.set(speed);
    motor2.set(speed);
  }
  public Command shooterCommand(double speed1){
    return run(coroutine -> {
      shoot(speed1);
      coroutine.park();
    }) 
  .whenExited(this::stop)
  .named("shoot");
  }



}
