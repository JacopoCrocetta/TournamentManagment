import { Inject, Injectable } from "@nestjs/common";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

interface CreateTournamentInput {}

@Injectable()
export class TournamentCreate {
  constructor(@Inject() tournamentRepository: ITournamentsRepository) {}

  create = async (input: CreateTournamentInput) => {
    return null;
  };
}
