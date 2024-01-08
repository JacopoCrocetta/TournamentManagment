import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";


export class TournamentCreatePresenter {
    constructor(private repository: ITournamentsRepository){}

    async deleteTournamentById(newTournament: Tournament){
        return await this.repository.create(newTournament);
    }
}