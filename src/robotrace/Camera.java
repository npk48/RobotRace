package robotrace;

/**
 * Implementation of a camera with a position and orientation. 
 */
class Camera {

    /** The position of the camera. */
    public Vector eye = new Vector(3f, 6f, 5f);

    /** The point to which the camera is looking. */
    public Vector center = Vector.O;

    /** The up vector. */
    public Vector up = Vector.Z;

    /**
     * Updates the camera viewpoint and direction based on the
     * selected camera mode.
     */
    public void update(GlobalState gs, Robot focus) {

        switch (gs.camMode) {
            
            // Helicopter mode
            case 1:
                setHelicopterMode(gs, focus);
                break;
                
            // Motor cycle mode    
            case 2:
                setMotorCycleMode(gs, focus);
                break;
                
            // First person mode    
            case 3:
                setFirstPersonMode(gs, focus);
                break;
                
            // Auto mode    
            case 4:
                setAutoMode(gs, focus);
                break;
                
            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {
        center = gs.cnt;
        eye.x = gs.cnt.x + gs.vDist*Math.cos(gs.phi)*Math.cos(gs.theta);
        eye.y = gs.cnt.y + gs.vDist*Math.cos(gs.phi)*Math.sin(gs.theta);
        eye.z = gs.cnt.z + gs.vDist*Math.sin(gs.phi);
    }

    /**
     * Computes eye, center, and up, based on the helicopter mode.
     * The camera should focus on the robot.
     */
    private void setHelicopterMode(GlobalState gs, Robot focus) {
        // code goes here ...
        center = focus.position;
        up = focus.direction;
        eye = center;
        eye = eye.add(new Vector(0, 0, 100));
    }

    /**
     * Computes eye, center, and up, based on the motorcycle mode.
     * The camera should focus on the robot.
     */
    private void setMotorCycleMode(GlobalState gs, Robot focus) {
        // code goes here ...
        center = focus.position;
        up = Vector.Z;
        eye = focus.direction.cross(Vector.Z);
        eye = eye.normalized();
        eye = eye.scale(50);
        eye = center.add(eye);
        eye = eye.add(new Vector(0, 0, 1));
       
    }

    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        // code goes here ...
        
        eye = focus.position;
        eye = eye.add(new Vector(0, 0, 0.5)); //camera at the upper surface of the track
        eye = eye.add(focus.direction.normalized().scale(2));
        up = Vector.Z;
        center = focus.direction; 
        center = center.normalized().scale(2);
        center = center.add(eye);
    }
    
    /**
     * Computes eye, center, and up, based on the auto mode.
     * The above modes are alternated.
     */
    private void setAutoMode(GlobalState gs, Robot focus) {
        // code goes here ...
        //Automatic change mode
        //Each cycle 20 seconds, 5 seconds each mode
        //Calculate time by using gs.tAnim
        int autoModeSelector = (int)gs.tAnim/5 % 4;

        switch (autoModeSelector) {
            case 0:
                setDefaultMode(gs);
                break;
            case 1:
                setHelicopterMode(gs, focus);
                break;
            case 2:
                setMotorCycleMode(gs, focus);
                break;
            case 3:
                setFirstPersonMode(gs, focus);
                break;
        }
    }

}
