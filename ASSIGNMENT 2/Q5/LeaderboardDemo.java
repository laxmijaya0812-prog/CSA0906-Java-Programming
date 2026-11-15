import java.util.*;

class Team {

    String name;
    int points;
    int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T> {

    private List<T> teams;

    public Leaderboard(Comparator<? super T> comparator) {
        teams = new ArrayList<>();
        this.comparator = comparator;
    }

    private Comparator<? super T> comparator;

    public void add(T team) {
        teams.add(team);
    }

    public List<T> getRankedTeams() {
        List<T> result = new ArrayList<>(teams);
        result.sort(comparator);
        return result;
    }
}

public class LeaderboardDemo {

    public static void main(String[] args) {

        Comparator<Team> byRank = (a, b) -> {

            int result = Integer.compare(b.points, a.points);

            if (result != 0)
                return result;

            result = Integer.compare(b.goalDiff, a.goalDiff);

            if (result != 0)
                return result;

            return a.name.compareTo(b.name);
        };

        Leaderboard<Team> leaderboard =
                new Leaderboard<>(byRank);

        leaderboard.add(new Team("Eagles", 9, 6));
        leaderboard.add(new Team("Bisons", 9, 4));
        leaderboard.add(new Team("Panthers", 9, 4));
        leaderboard.add(new Team("Cobras", 6, 1));

        System.out.println("Leaderboard:");

        for (Team team : leaderboard.getRankedTeams()) {
            System.out.println(team);
        }
    }
}

Expected Output

Leaderboard:
Eagles(9,6)
Bisons(9,4)
Panthers(9,4)
Cobras(6,1)

Ranking Rules

Higher points first.

If points are equal, higher goal difference first.

If both are equal, team name is considered alphabetically.
