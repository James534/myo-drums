// Copyright (C) 2013-2014 Thalmic Labs Inc.
// Distributed under the Myo SDK license agreement. See LICENSE.txt for details.

// This sample illustrates how to interface with multiple Myo armbands and distinguish between them.

#include <iostream>
#include <stdexcept>
#include <vector>

#include <myo/myo.hpp>

class PrintMyoEvents : public myo::DeviceListener {
public:
    // Every time Myo Connect successfully pairs with a Myo armband, this function will be called.
    //
    // You can rely on the following rules:
    //  - onPair() will only be called once for each Myo device
    //  - no other events will occur involving a given Myo device before onPair() is called with it
    //
    // If you need to do some kind of per-Myo preparation before handling events, you can safely do it in onPair().
    void onPair(myo::Myo* myo, uint64_t timestamp, myo::FirmwareVersion firmwareVersion)
    {
        // Print out the MAC address of the armband we paired with.

        // The pointer address we get for a Myo is unique - in other words, it's safe to compare two Myo pointers to
        // see if they're referring to the same Myo.

        // Add the Myo pointer to our list of known Myo devices. This list is used to implement identifyMyo() below so
        // that we can give each Myo a nice short identifier.
        knownMyos.push_back(myo);

        // Now that we've added it to our list, get our short ID for it and print it out.
        std::cout << "Paired with " << identifyMyo(myo) << "." << std::endl;
    }
    // onArmRecognized() is called whenever Myo has recognized a setup gesture after someone has put it on their
    // arm. This lets Myo know which arm it's on and which way it's facing.
    void onArmRecognized(myo::Myo* myo, uint64_t timestamp, myo::Arm arm, myo::XDirection xDirection)
    {
        onArm[identifyMyo(myo)] = true;
        whichArm[identifyMyo(myo)] = arm;
    }
    
    // onArmLost() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
    // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
    // when Myo is moved around on the arm.
    void onArmLost(myo::Myo* myo, uint64_t timestamp)
    {
        onArm[identifyMyo(myo)] = false;
    }
    void onPose(myo::Myo* myo, uint64_t timestamp, myo::Pose pose)
    {
        std::cout << "POSE:" << (whichArm[identifyMyo(myo)] == myo::armLeft ? "L" : "R") << ',' << pose.toString() << std::endl;
        //print(myo);
    }

    void onConnect(myo::Myo* myo, uint64_t timestamp, myo::FirmwareVersion firmwareVersion)
    {
        std::cout << "Myo " << identifyMyo(myo) << " has connected." << std::endl;
    }

    void onDisconnect(myo::Myo* myo, uint64_t timestamp)
    {
        std::cout << "Myo " << identifyMyo(myo) << " has disconnected." << std::endl;
    }

    // This is a utility function implemented for this sample that maps a myo::Myo* to a unique ID starting at 1.
    // It does so by looking for the Myo pointer in knownMyos, which onPair() adds each Myo into as it is paired.
    size_t identifyMyo(myo::Myo* myo) {
        // Walk through the list of Myo devices that we've seen pairing events for.
        for (size_t i = 0; i < knownMyos.size(); ++i) {
            // If two Myo pointers compare equal, they refer to the same Myo device.
            if (knownMyos[i] == myo) {
                return i + 1;
            }
        }

        return 0;
    }

    // onUnpair() is called whenever the Myo is disconnected from Myo Connect by the user.
    void onUnpair(myo::Myo* myo, uint64_t timestamp)
    {
        // We've lost a Myo.
        // Let's clean up some leftover state.
        roll_w[identifyMyo(myo)] = 0;
        pitch_w[identifyMyo(myo)] = 0;
        yaw_w[identifyMyo(myo)] = 0;
        onArm[identifyMyo(myo)] = false;
    }
    
    // onOrientationData() is called whenever the Myo device provides its current orientation, which is represented
    // as a unit quaternion.
    void onOrientationData(myo::Myo* myo, uint64_t timestamp, const myo::Quaternion<float>& quat)
    {
        using std::atan2;
        using std::asin;
        using std::sqrt;
        
        // Calculate Euler angles (roll, pitch, and yaw) from the unit quaternion.
        float roll = atan2(2.0f * (quat.w() * quat.x() + quat.y() * quat.z()),
                           1.0f - 2.0f * (quat.x() * quat.x() + quat.y() * quat.y()));
        float pitch = asin(2.0f * (quat.w() * quat.y() - quat.z() * quat.x()));
        float yaw = atan2(2.0f * (quat.w() * quat.z() + quat.x() * quat.y()),
                          1.0f - 2.0f * (quat.y() * quat.y() + quat.z() * quat.z()));
        
        // Convert the floating point angles in radians to a scale from 0 to 20.
        roll_w[identifyMyo(myo)] = static_cast<int>((roll + (float)M_PI)/(M_PI * 2.0f) * 18);
        pitch_w[identifyMyo(myo)] = static_cast<int>((pitch + (float)M_PI/2.0f)/M_PI * 18);
        yaw_w[identifyMyo(myo)] = static_cast<int>((yaw + (float)M_PI)/(M_PI * 2.0f) * 18);
    }
    
    void onAccelerometerData(myo::Myo* myo, uint64_t timestamp, const myo::Vector3<float>& accel)
    {
        xAccel[identifyMyo(myo)] = accel.x();
        yAccel[identifyMyo(myo)] = accel.y();
        zAccel[identifyMyo(myo)] = accel.z();
    }
    
    void onGyroscopeData(myo::Myo* myo, uint64_t timestamp, const myo::Vector3<float>& gyro)
    {
        xGyro[identifyMyo(myo)] = gyro.x();
        yGyro[identifyMyo(myo)] = gyro.y();
        zGyro[identifyMyo(myo)] = gyro.z();
    }
    // There are other virtual functions in DeviceListener that we could override here, like onAccelerometerData().
    // For this example, the functions overridden above are sufficient.
    
    // We define this function to print the current values that were updated by the on...() functions above.
    void print(myo::Myo* myo)
    {
        
        // Clear the current line
        std::cout << '\r';
        
        if (onArm[identifyMyo(myo)]) {
            // Print out the orientation. Orientation data is always available, even if no arm is currently recognized.
            std::cout << roll_w[identifyMyo(myo)] << ',' << pitch_w[identifyMyo(myo)] << ',' << yaw_w[identifyMyo(myo)] << ',' << xAccel[identifyMyo(myo)] << ',' << yAccel[identifyMyo(myo)] << ',' << zAccel[identifyMyo(myo)] << ',' << xGyro[identifyMyo(myo)] << ',' << yGyro[identifyMyo(myo)] << ',' << zGyro[identifyMyo(myo)] << ',' << (whichArm[identifyMyo(myo)] == myo::armLeft ? "L" : "R") << std::endl;
        }
        std::cout << std::flush;
    }
    
    // These values are set by onArmRecognized() and onArmLost() above.
    std::vector<bool> onArm = std::vector<bool>(2, false);
    std::vector<myo::Arm> whichArm = std::vector<myo::Arm>(2);
    
    std::vector<int> xAccel = std::vector<int>(2);
    std::vector<int> yAccel = std::vector<int>(2);
    std::vector<int> zAccel = std::vector<int>(2);
    std::vector<int> xGyro = std::vector<int>(2);
    std::vector<int> yGyro = std::vector<int>(2);
    std::vector<int> zGyro = std::vector<int>(2);
    
    // These values are set by onOrientationData() and onPose() above.
    std::vector<int> roll_w = std::vector<int>(2);
    std::vector<int> pitch_w = std::vector<int>(2);
    std::vector<int> yaw_w = std::vector<int>(2);
    
    // We store each Myo pointer that we pair with in this list, so that we can keep track of the order we've seen
    // each Myo and give it a unique short identifier (see onPair() and identifyMyo() above).
    std::vector<myo::Myo*> knownMyos;
};

int main(int argc, char** argv)
{
    try {
        myo::Hub hub("com.example.multiple-myos");

        // Instantiate the PrintMyoEvents class we defined above, and attach it as a listener to our Hub.
        PrintMyoEvents printer;
        hub.addListener(&printer);
        
        while (1) {
            // Process events for 10 milliseconds at a time.
            hub.run(10);
            for (int i = 0; i < printer.knownMyos.size(); i++){
                printer.print(printer.knownMyos[i]);
            }
        }
    } catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
        std::cerr << "Press enter to continue.";
        std::cin.ignore();
        return 1;
    }
}
