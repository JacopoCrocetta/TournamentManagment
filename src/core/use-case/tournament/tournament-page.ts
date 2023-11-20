import { Inject, Injectable, Logger } from "@nestjs/common";
import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

@Injectable()
export class TournamentPage {
  constructor(@Inject() private repository: ITournamentsRepository) {}

  async getById(id: number): Promise<Tournament> {
    Logger.log("RETRIEVING TOURNAMENT WITH ID " + id + "...");
    return await this.repository.getById(id);
  }

  async getAll(): Promise<Tournament[]> {
    Logger.log("RETRIEVING ALL TOURNAMENT...");
    return await this.repository.getAll();
  }
}
