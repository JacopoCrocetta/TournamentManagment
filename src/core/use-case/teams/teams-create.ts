import { Inject, Injectable } from "@nestjs/common";
import { ITeamsRepository } from "src/core/repository/teams.repository";

interface CreateTeamsInput {}

@Injectable()
export class TeamsCreate {
  constructor(@Inject() teamsRepository: ITeamsRepository) {}

  create = async (input: CreateTeamsInput) => {
    return null;
  };
}
