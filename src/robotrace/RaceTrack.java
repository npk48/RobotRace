package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;

    /** Array with 3N control points, where N is the number of segments. */
    private Vector[] controlPoints = null;
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }
    
    /**
     * Constructor for a spline track.
     */
    public RaceTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
    }

    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        if (null == controlPoints) {
            // draw the test track
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            //Draw the bound lines of the track (5 lines indicate 4 lanes)
            for (int laneBound = 0; laneBound <= 4; laneBound++) {
                gl.glBegin(GL2.GL_LINE_LOOP);
                double sampleDistance = 0.01;
                for (double i = 0; i <= 1; i += sampleDistance) {
                    Vector radius;
                    radius = getTangent(i).cross(new Vector(0, 0, 1));
                    radius.normalized();
                    radius.x *= laneWidth;
                    radius.y *= laneWidth;
                    gl.glColor3f(0f, 0f, 0f);
                    gl.glVertex3d(getPoint(i).x + (laneBound - 2) * 2 * radius.x, 
                            getPoint(i).y + (laneBound - 2) * 2 * radius.y, 
                            getPoint(i).z);
                }
                gl.glEnd();
            }
            for (int laneBound = 0; laneBound <= 3; laneBound++) {
                gl.glBegin(GL2.GL_TRIANGLE_STRIP);
                double sampleDistance = 0.0001;
                for (double i = 0; i <= 1; i += sampleDistance) {
                    Vector radius;
                    radius = getTangent(i).cross(new Vector(0, 0, 1));
                    radius.normalized();
                    radius.x *= laneWidth;
                    radius.y *= laneWidth;
                    gl.glColor3f(0.6f, 0.6f, 0.6f);
                    gl.glVertex3d(getPoint(i).x + (laneBound - 2) * 2 * radius.x, 
                            getPoint(i).y + (laneBound - 2) * 2 * radius.y, 
                            getPoint(i).z);
                    gl.glVertex3d(getPoint(i + sampleDistance).x + (laneBound - 1) * 2 * radius.x, 
                            getPoint(i + sampleDistance).y + (laneBound - 1) * 2 * radius.y, 
                            getPoint(i + sampleDistance).z);
                }
                gl.glEnd();
            }
            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            double sampleDistance = 0.0001;
            for(double i = 0; i < 1; i += sampleDistance){
                Vector radius = new Vector(0,0,0);
                radius = getTangent(i).cross(new Vector(0,0,1));
                radius.normalized();
                radius.x *= laneWidth;
                radius.y *= laneWidth;
                gl.glColor3f(0.6f, 0.6f, 0.6f);
                gl.glVertex3d(getPoint(i).x + 2 * 2 * radius.x, 
                        getPoint(i).y + 2 * 2 * radius.y,
                        getPoint(i).z);
                gl.glVertex3d(getPoint(i + sampleDistance).x + 2 * 2 * radius.x,
                        getPoint(i + sampleDistance).y + 2 * 2 * radius.y,
                        getPoint(i+ sampleDistance).z - 1);
            }
            gl.glEnd();
            
            gl.glDisable(GL2.GL_COLOR_MATERIAL);
        } else {
            // draw the spline track
        }
    }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t) {
        if (null == controlPoints) {
            Vector pointAtT = getPoint(t);
            Vector tangentAtT = getTangent(t);
            Vector normal = tangentAtT.cross(new Vector(0, 0, 1));
            normal = normal.normalized();
            return pointAtT.add(normal.scale((lane - 1) * 2 * laneWidth));
        } else {
            Vector pointAtT = getPoint(t);
            return pointAtT;
        }
    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t) {
        if (null == controlPoints) {
            return getTangent(t); 
        } else {
            return Vector.O; // <- code goes here
        }
    }

    /**
     * Returns a point on the test track at 0 <= t < 1.
     */
    public Vector getPoint(double t) {
        Vector pointAtT;
        /*get the point vector when t*/
        pointAtT = new Vector((10 * Math.cos(2 * Math.PI * t)), 
                (14 * Math.sin(2 * Math.PI * t)), 
                1);
        
        return pointAtT;
    }

    /**
     * Returns a tangent on the test track at 0 <= t < 1.
     */
    private Vector getTangent(double t) {
        double tangentX;
        double tangentY;
        double tangentZ;
        double length;
        Vector tangentAtT;
        /* using formula tangent vector T(t) = P'(t)/|P(t)|*/
        /* where P'(t) is the derivation of P(t)*/
        //differentiate of x y z
        tangentX = -20 * Math.PI * Math.sin(2 * Math.PI * t);
        tangentY = 28 * Math.PI * Math.cos(2 * Math.PI * t);
        tangentZ = 0;
        length = Math.sqrt((tangentX * tangentX) + (tangentY * tangentY) + (tangentZ * tangentZ));
            
        tangentX = tangentX / length;
        tangentY = tangentY / length;
        tangentZ = tangentZ / length;
            
        tangentAtT = new Vector(tangentX, tangentY, tangentZ);
        return tangentAtT;
    }
    
    /**
     * Returns a point on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    private Vector getCubicBezierPoint(double t, Vector P0, Vector P1,
                                                 Vector P2, Vector P3) {
        return Vector.O; // <- code goes here
    }
    
    /**
     * Returns a tangent on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    private Vector getCubicBezierTangent(double t, Vector P0, Vector P1,
                                                   Vector P2, Vector P3) {
        return Vector.O; // <- code goes here
    }
}
