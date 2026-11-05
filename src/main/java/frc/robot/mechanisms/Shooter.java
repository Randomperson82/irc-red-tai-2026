package frc.robot.mechanisms;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityVoltage;


public class Shooter implements Mechanism {
  private final SparkMax feederMotor = new SparkMax(3, MotorType.kBrushless);
  private final TalonFX shooter = new TalonFX(4);
      // INTAKE_DOWN,
  private SparkMaxConfig motorConfig1 = new SparkMaxConfig();
  private TalonFX shooterConfig = new TalonFXConfiguration();

  public Shooter() {
    motorConfig1.inverted(true);
    motorConfig1.smartCurrentLimit(40);
    motorConfig1.secondaryCurrentLimit(80);
    feederMotor.configure(motorConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

  }

  private void stop() {
    feederMotor.stopMotor();
  }

  private void shoot(double speed) {
    feederMotor.set(speed);
  }

  public Command shooterCommand(double speed1) {
    return run(coroutine -> {
      shoot(speed1);
      coroutine.park();
    })
        .whenExited(this::stop)
        .named("shoot");
  }

}
