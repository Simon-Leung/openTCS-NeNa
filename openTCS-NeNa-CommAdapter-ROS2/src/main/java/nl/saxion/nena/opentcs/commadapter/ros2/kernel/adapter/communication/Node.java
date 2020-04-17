package nl.saxion.nena.opentcs.commadapter.ros2.kernel.adapter.communication;

import action_msgs.msg.GoalStatusArray;
import geometry_msgs.msg.Point;
import geometry_msgs.msg.Pose;
import geometry_msgs.msg.PoseStamped;
import lombok.Getter;
import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.executors.SingleThreadedExecutor;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.publisher.Publisher;

import java.util.concurrent.TimeUnit;

/**
 * Class that holds an an instances of node and of all its publishers and subscriptions.
 */
public class Node extends BaseComposableNode {
    @Getter
    private Publisher<PoseStamped> goalPublisher;

    public Node(NodeListener nodeListener) {
        super("opentcs"); // Node Name

        // Publisher for sending a navigation goal
        this.goalPublisher = node.createPublisher(
                PoseStamped.class,
                "/move_base_simple/goal"
        );
        SingleThreadedExecutor hoi = new SingleThreadedExecutor();

        // Subscriber for navigation status
        node.createSubscription(
                GoalStatusArray.class,
                "/NavigateToPose/_action/status",
                nodeListener::onNewGoalStatusArray);

        // Subscription for current location
        // TODO, see: topic /amclpose

        // Subscription for battery data (TurtleBot3 specific)
        // TODO, see: http://docs.ros.org/api/turtlebot3_msgs/html/msg/SensorState.html
    }

    /**
     * Stop the node
     */
    public void stop() {
        RCLJava.shutdown();
    }
}