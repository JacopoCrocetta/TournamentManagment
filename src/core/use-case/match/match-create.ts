import { Inject, Injectable } from "@nestjs/common";
import { IMatchRepository } from "src/core/repository/match.repository";

interface CreateMatchInput {}

@Injectable()
export class MatchCreate {
  constructor(@Inject() matchRepository: IMatchRepository) {}

  create = async (input: CreateMatchInput) => {
    return null;
  };
}
