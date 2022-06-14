  package frc.robot;

  import edu.wpi.first.wpilibj.TimedRobot;

  import com.ctre.phoenix.motorcontrol.ControlMode; 
  import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
  import edu.wpi.first.wpilibj.XboxController;
  import edu.wpi.first.wpilibj.Timer;
  import edu.wpi.first.wpilibj.drive.DifferentialDrive;
  import edu.wpi.first.wpilibj.Compressor; 
  import edu.wpi.first.wpilibj.DoubleSolenoid; 
  import edu.wpi.first.wpilibj.PneumaticsModuleType; 
  import edu.wpi.first.wpilibj.GenericHID;

  public class Robot extends TimedRobot implements Constants {
    protected Timer timer; 
    protected double forwardTorque, lateralTorque; 

    protected final WPI_VictorSPX leftFront  = new WPI_VictorSPX(LEFT_FRONT); 
    protected final WPI_VictorSPX rightFront = new WPI_VictorSPX(RIGHT_FRONT);   
    protected final WPI_VictorSPX rightFollower = new WPI_VictorSPX(RIGHT_FOLLOWER); 
    protected final WPI_VictorSPX leftFollower = new WPI_VictorSPX(LEFT_FOLLOWER); 

    private final Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);
    private final DoubleSolenoid solenoid =  new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1); 


    DifferentialDrive drive = new DifferentialDrive(leftFront, rightFront); 

    public XboxController controller = new XboxController(JOYSTICK_PORT);  

    @Override
    public void autonomousInit() {
      timer = new Timer(); 
      timer.reset(); 
      timer.start();
    }

    @Override
    public void autonomousPeriodic() {
      if (timer.get() < 2.0) {
        drive.arcadeDrive(100, 30);
      }
    }

    @Override
    public void teleopInit() {
      forwardTorque = 0; 
      lateralTorque = 0; 
      compressor.disable();
    }

    @Override
    public void teleopPeriodic() {
      forwardTorque = controller.getRawAxis(JOYSTICK_LEFT); 
      lateralTorque = controller.getRawAxis(JOYSTICK_RIGHT); 
      leftFollower.follow(leftFront); 
      rightFollower.follow(rightFront); 
      drive.arcadeDrive(lateralTorque, forwardTorque);

      //Solenoid 
      if (controller.getLeftBumper()){
        solenoid.set(DoubleSolenoid.Value.kForward);
      } else if (controller.getRightBumper()) {
        solenoid.set(DoubleSolenoid.Value.kReverse); 
      }

      if (controller.getAButton()){
        compressor.enableDigital();
      }else if (controller.getBButton()){
        compressor.disable();
      }


    } 
  } 
