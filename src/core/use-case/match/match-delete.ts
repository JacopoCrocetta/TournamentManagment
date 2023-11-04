import { Inject } from "@nestjs/common";
import { IMatchRepository } from "src/core/repository/match.repository";

export class MatchDelete {
  constructor(@Inject() private matchRepository: IMatchRepository) {}

  delete = async (id: number): Promise<boolean> => {
    this.matchRepository.delete(id);

    return this.matchRepository.getById(id) == null;
  };
}
