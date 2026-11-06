package frc.robot.mechanisms;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DutyCycle;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityVoltage;


public class Shooter implements Mechanism {
  // give speed of motor on imput and it outputs the voltage for the motor
  private final VelocityVoltage VelocityVoltageRequest = new VelocityVoltage(0);
  // input is how much percent power the motor outputs
  private final DutyCycleOut DutyCycleRequest = new DutyCycleOut(0);

  private final SparkMax feederMotor = new SparkMax(3, MotorType.kBrushless);
  private final TalonFX shooter = new TalonFX(4);

  private SparkMaxConfig motorConfig1 = new SparkMaxConfig();
  private TalonFXConfiguration shooterConfig = new TalonFXConfiguration();

  public Shooter() {
    motorConfig1.inverted(true);
    motorConfig1.smartCurrentLimit( 40);
    motorConfig1.secondaryCurrentLimit(80);
    feederMotor.configure(motorConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    shooterConfig.MotorOutput.withInverted(InvertedValue.Clockwise_Positive);
    shooterConfigureCurrentLimits(shooterConfig);
    shooter.getConfigurator().apply(shooterConfig);
  }

  private void shooterConfigureCurrentLimits(TalonFXConfiguration config) {
    config.CurrentLimits.StatorCurrentLimit = 60.0;
    config.CurrentLimits.StatorCurrentLimitEnable = true;
    config.CurrentLimits.SupplyCurrentLimit = 30.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
  }

  private void stop() {
    feederMotor.stopMotor();
    shooter.stopMotor();
  }

  private void shoot(double feederSpeed, double shooterSpeed) {
    feederMotor.set(feederSpeed);
    shooter.setControl(DutyCycleRequest.withOutput(shooterSpeed));
  }

  public Command shooterCommand(double feederSpeed, double shooterSpeed) {
    return run(coroutine -> {
      shoot(feederSpeed, shooterSpeed);
      coroutine.park();
    })
        .whenExited(this::stop)
        .named("shoot");
  }

}
