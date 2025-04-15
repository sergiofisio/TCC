export default function UserExercises({ user }) {
  return (
    <div className="user-exercises">
      <p>
        <strong>Meditações realizadas:</strong>{" "}
        {user.meditations_user?.length || 0}
      </p>
      <p>
        <strong>Respirações realizadas:</strong>{" "}
        {user.breaths_user?.length || 0}
      </p>
    </div>
  );
}
