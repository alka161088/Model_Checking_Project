package modelCheckCTL.util;

import java.util.ArrayList;
import java.util.List;

public class State {
    /// <summary>
/// Constructor
/// </summary>\\
    public String stateName;
    public List<String> atoms;

    public State() {
        atoms = new ArrayList<String>();
    }

    /// <smmary>
    /// Overloaded constructor
    /// </summary>
    /// <param name="stateName"></param>
    public State(String stateName) {
        this.stateName = stateName;
    }


    /// <summary>
    /// Implement Equals method
    /// </summary>
    /// <param name="other"></param>
    /// <returns></returns>
    public boolean equals(Object obj) {
        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof State)) {
            return false;
        }

        State other = (State) obj;
        if (this.stateName.equals(other.stateName)) {
            return true;
        }
        return false;
    }
}