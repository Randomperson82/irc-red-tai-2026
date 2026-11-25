package frc.robot.mechanisms;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

public class Feeder implements Mechanism {

  private final SparkMax feederMotor = new SparkMax(3, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig1 = new SparkMaxConfig();

  public Feeder() {
    motorConfig1.inverted(true);
    motorConfig1.smartCurrentLimit(40);
    motorConfig1.secondaryCurrentLimit(80);
    feederMotor.configure(motorConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

  }

  private void stop() {
    feederMotor.stopMotor();
  }



  private void feederSpeed(double motorSpeed) {
    feederMotor.set(motorSpeed);
  }

  public Command feedCommand(double motorSpeed) {
    return run(coroutine -> {
      feederSpeed(motorSpeed);
      coroutine.park();
    })
    .whenExited(this::stop)
    .named("feed");
  }


}
